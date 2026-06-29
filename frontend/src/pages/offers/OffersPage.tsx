import { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useForm, useFieldArray } from 'react-hook-form';
import { motion } from 'framer-motion';
import { Plus, Trash2, Scale, Trophy } from 'lucide-react';
import toast from 'react-hot-toast';
import { Card } from '../../components/ui/Card';
import { Button } from '../../components/ui/Button';
import { Input } from '../../components/ui/Input';
import { Badge } from '../../components/ui/Badge';
import { CardSkeleton } from '../../components/ui/Skeleton';
import { Modal } from '../../components/ui/Modal';
import { offersApi } from '../../api/offers';
import type { OfferComparisonRequest } from '../../types';

export function OffersPage() {
  const [showForm, setShowForm] = useState(false);
  const queryClient = useQueryClient();

  const { data: offers, isLoading } = useQuery({
    queryKey: ['offers'],
    queryFn: offersApi.getAll,
  });

  const deleteMutation = useMutation({
    mutationFn: offersApi.delete,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['offers'] });
      toast.success('Offer analysis deleted');
    },
  });

  if (isLoading) return <CardSkeleton />;

  return (
    <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-900 dark:text-white">Offer Analysis</h1>
          <p className="text-gray-600 dark:text-gray-400 mt-1">Compare job offers with AI-powered analysis</p>
        </div>
        <Button onClick={() => setShowForm(true)}>
          <Plus className="w-4 h-4 mr-2" />
          Compare Offers
        </Button>
      </div>

      {offers && offers.length === 0 ? (
        <Card className="p-12 text-center">
          <Scale className="w-12 h-12 text-gray-400 mx-auto mb-4" />
          <h3 className="text-lg font-medium text-gray-900 dark:text-white">No offer analyses yet</h3>
          <p className="text-gray-600 dark:text-gray-400 mt-1">Compare multiple job offers to find the best one</p>
          <Button onClick={() => setShowForm(true)} className="mt-4">
            <Plus className="w-4 h-4 mr-2" />
            Compare Offers
          </Button>
        </Card>
      ) : (
        <div className="space-y-4">
          {(offers || []).map((offer: Record<string, unknown>, i: number) => (
            <motion.div
              key={offer.id as number}
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: i * 0.05 }}
            >
              <Card className="p-6">
                <div className="flex items-start justify-between mb-4">
                  <div className="flex items-center gap-3">
                    <div className="p-2 bg-amber-50 dark:bg-amber-900/20 rounded-lg">
                      <Trophy className="w-5 h-5 text-amber-600" />
                    </div>
                    <div>
                      <h3 className="font-medium text-gray-900 dark:text-white">
                        Offer Comparison #{offer.id as number}
                      </h3>
                      <p className="text-xs text-gray-500">
                        {new Date(offer.createdAt as string).toLocaleDateString()}
                      </p>
                    </div>
                  </div>
                  <div className="flex items-center gap-2">
                    {offer.topPickIndex !== undefined && (
                      <Badge variant="success">Top Pick: #{(offer.topPickIndex as number) + 1}</Badge>
                    )}
                    <Button
                      size="sm"
                      variant="ghost"
                      className="text-red-500"
                      onClick={() => deleteMutation.mutate(offer.id as number)}
                    >
                      <Trash2 className="w-4 h-4" />
                    </Button>
                  </div>
                </div>
                {offer.recommendation ? (
                  <div className="bg-gray-50 dark:bg-gray-800/50 rounded-lg p-4">
                    <p className="text-sm text-gray-700 dark:text-gray-300 whitespace-pre-wrap line-clamp-4">
                      {String(offer.recommendation)}
                    </p>
                  </div>
                ) : null}
              </Card>
            </motion.div>
          ))}
        </div>
      )}

      <Modal isOpen={showForm} onClose={() => setShowForm(false)} title="Compare Job Offers" size="xl">
        <OfferForm onSuccess={() => { setShowForm(false); queryClient.invalidateQueries({ queryKey: ['offers'] }); }} />
      </Modal>
    </motion.div>
  );
}

function OfferForm({ onSuccess }: { onSuccess: () => void }) {
  const [loading, setLoading] = useState(false);
  const { register, handleSubmit, control } = useForm<OfferComparisonRequest>({
    defaultValues: {
      offers: [
        { companyName: '', jobTitle: '', baseSalary: 0 },
        { companyName: '', jobTitle: '', baseSalary: 0 },
      ],
    },
  });

  const { fields, append, remove } = useFieldArray({ control, name: 'offers' });

  const onSubmit = async (data: OfferComparisonRequest) => {
    setLoading(true);
    try {
      await offersApi.create(data);
      toast.success('Offers analyzed!');
      onSuccess();
    } catch {
      toast.error('Analysis failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
      {fields.map((field, index) => (
        <div key={field.id} className="p-4 border border-gray-200 dark:border-gray-700 rounded-lg space-y-3">
          <div className="flex items-center justify-between">
            <h4 className="font-medium text-gray-900 dark:text-white">Offer #{index + 1}</h4>
            {fields.length > 2 && (
              <Button size="sm" variant="ghost" className="text-red-500" onClick={() => remove(index)}>
                <Trash2 className="w-3 h-3" />
              </Button>
            )}
          </div>
          <div className="grid grid-cols-2 gap-3">
            <Input placeholder="Company Name" {...register(`offers.${index}.companyName`, { required: true })} />
            <Input placeholder="Job Title" {...register(`offers.${index}.jobTitle`, { required: true })} />
            <Input placeholder="Base Salary (₹)" type="number" {...register(`offers.${index}.baseSalary`, { valueAsNumber: true })} />
            <Input placeholder="Annual Bonus (₹)" type="number" {...register(`offers.${index}.annualBonus`, { valueAsNumber: true })} />
            <Input placeholder="City" {...register(`offers.${index}.city`)} />
            <Input placeholder="Industry" {...register(`offers.${index}.industry`)} />
          </div>
          <Input placeholder="Benefits (comma-separated)" {...register(`offers.${index}.benefits`)} />
        </div>
      ))}

      <Button type="button" variant="outline" onClick={() => append({ companyName: '', jobTitle: '', baseSalary: 0 })} className="w-full">
        <Plus className="w-4 h-4 mr-2" />
        Add Another Offer
      </Button>

      <Button type="submit" loading={loading} className="w-full" size="lg">
        <Scale className="w-4 h-4 mr-2" />
        Analyze Offers
      </Button>
    </form>
  );
}
